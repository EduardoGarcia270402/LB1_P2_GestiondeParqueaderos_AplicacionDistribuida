import { ConflictException } from '@nestjs/common';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Test } from '@nestjs/testing';
import { Repository } from 'typeorm';
import { Usuario } from '../usuarios/entities/usuario.entity';
import { Rol } from './entities/rol.entity';
import { UsuarioRol } from './entities/usuario-rol.entity';
import { RolesService } from './roles.service';

describe('RolesService', () => {
  let service: RolesService;
  let rolesRepository: jest.Mocked<Repository<Rol>>;
  let usuarioRolRepository: jest.Mocked<Repository<UsuarioRol>>;
  let usuariosRepository: jest.Mocked<Repository<Usuario>>;

  beforeEach(async () => {
    const createRepositoryMock = () => ({
      findOneBy: jest.fn(),
      create: jest.fn((value) => value),
      save: jest.fn(async (value) => value),
      find: jest.fn(),
    });
    const module = await Test.createTestingModule({
      providers: [
        RolesService,
        {
          provide: getRepositoryToken(Rol),
          useValue: createRepositoryMock(),
        },
        {
          provide: getRepositoryToken(UsuarioRol),
          useValue: createRepositoryMock(),
        },
        {
          provide: getRepositoryToken(Usuario),
          useValue: createRepositoryMock(),
        },
      ],
    }).compile();

    service = module.get(RolesService);
    rolesRepository = module.get(getRepositoryToken(Rol));
    usuarioRolRepository = module.get(getRepositoryToken(UsuarioRol));
    usuariosRepository = module.get(getRepositoryToken(Usuario));
  });

  it('asigna un rol activo a un usuario activo', async () => {
    usuariosRepository.findOneBy.mockResolvedValue({
      idPerson: 'user-id',
      active: true,
    } as Usuario);
    rolesRepository.findOneBy.mockResolvedValue({
      id: 'role-id',
      name: 'CLIENTE',
      active: true,
    } as Rol);
    usuarioRolRepository.findOneBy.mockResolvedValue(null);

    const asignacion = await service.asignarRol('user-id', 'role-id');

    expect(asignacion).toMatchObject({
      idUser: 'user-id',
      idRole: 'role-id',
    });
  });

  it('rechaza una asignacion duplicada', async () => {
    usuariosRepository.findOneBy.mockResolvedValue({
      idPerson: 'user-id',
      active: true,
    } as Usuario);
    rolesRepository.findOneBy.mockResolvedValue({
      id: 'role-id',
      active: true,
    } as Rol);
    usuarioRolRepository.findOneBy.mockResolvedValue({
      idUser: 'user-id',
      idRole: 'role-id',
    } as UsuarioRol);

    await expect(
      service.asignarRol('user-id', 'role-id'),
    ).rejects.toBeInstanceOf(ConflictException);
  });
});
