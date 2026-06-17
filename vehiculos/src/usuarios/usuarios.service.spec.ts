import { ConflictException } from '@nestjs/common';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Test } from '@nestjs/testing';
import { Repository } from 'typeorm';
import { Persona } from '../personas/entities/persona.entity';
import { Usuario } from './entities/usuario.entity';
import { UsuariosService } from './usuarios.service';

describe('UsuariosService', () => {
  let service: UsuariosService;
  let usuariosRepository: jest.Mocked<Repository<Usuario>>;
  let personasRepository: jest.Mocked<Repository<Persona>>;

  beforeEach(async () => {
    const usuariosMock = {
      findOneBy: jest.fn(),
      findOne: jest.fn(),
      create: jest.fn((value) => value),
      save: jest.fn(),
      find: jest.fn(),
    };
    const personasMock = {
      findOneBy: jest.fn(),
    };
    const module = await Test.createTestingModule({
      providers: [
        UsuariosService,
        {
          provide: getRepositoryToken(Usuario),
          useValue: usuariosMock,
        },
        {
          provide: getRepositoryToken(Persona),
          useValue: personasMock,
        },
      ],
    }).compile();

    service = module.get(UsuariosService);
    usuariosRepository = module.get(getRepositoryToken(Usuario));
    personasRepository = module.get(getRepositoryToken(Persona));
  });

  it('crea un usuario con la contrasena transformada en hash', async () => {
    const persona = {
      id: 'person-id',
      active: true,
      firstName: 'Eduardo',
      middleName: 'Andres',
      lastName: 'Garcia',
    } as Persona;
    personasRepository.findOneBy.mockResolvedValue(persona);
    usuariosRepository.findOneBy.mockResolvedValue(null);
    usuariosRepository.save.mockImplementation(async (value) => value);
    usuariosRepository.findOne.mockResolvedValue({
      idPerson: persona.id,
      username: 'eagarcia1',
      persona,
    } as Usuario);
    usuariosRepository.find.mockResolvedValue([]);

    const creado = await service.create({
      personId: persona.id,
      password: 'Password123',
    });

    const guardado = usuariosRepository.save.mock.calls[0][0] as Usuario;
    expect(guardado.username).toBe('eagarcia1');
    expect(guardado.passwordHash).not.toBe('Password123');
    expect(guardado.passwordHash).toMatch(/^\$2[aby]\$/);
    expect(creado).not.toHaveProperty('passwordHash');
  });

  it('rechaza crear dos usuarios para la misma persona', async () => {
    personasRepository.findOneBy.mockResolvedValue({
      id: 'person-id',
      active: true,
    } as Persona);
    usuariosRepository.findOneBy.mockResolvedValueOnce({
      idPerson: 'person-id',
    } as Usuario);

    await expect(
      service.create({
        personId: 'person-id',
        password: 'Password123',
      }),
    ).rejects.toBeInstanceOf(ConflictException);
  });
});
