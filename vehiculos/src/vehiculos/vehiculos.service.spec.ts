import { getRepositoryToken } from '@nestjs/typeorm';
import { Test, TestingModule } from '@nestjs/testing';
import { Repository } from 'typeorm';
import { Vehiculo } from './entities/vehiculo.entity';
import { VehiculosService } from './vehiculos.service';

describe('VehiculosService', () => {
  let service: VehiculosService;
  let repository: jest.Mocked<Repository<Vehiculo>>;

  beforeEach(async () => {
    const repositoryMock = {
      findOneBy: jest.fn(),
      find: jest.fn(),
      save: jest.fn(),
      merge: jest.fn(),
      remove: jest.fn(),
    };
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        VehiculosService,
        {
          provide: getRepositoryToken(Vehiculo),
          useValue: repositoryMock,
        },
      ],
    }).compile();

    service = module.get(VehiculosService);
    repository = module.get(getRepositoryToken(Vehiculo));
  });

  it('lists stored vehicles', async () => {
    repository.find.mockResolvedValue([]);
    await expect(service.findAll()).resolves.toEqual([]);
  });

  it('rejects a duplicated plate', async () => {
    repository.findOneBy.mockResolvedValue({ placa: 'ABC-1234' } as Vehiculo);

    await expect(
      service.create({
        tipo: 'Auto',
        datos: {
          placa: 'ABC-1234',
          marca: 'Toyota',
          modelo: 'Corolla',
          color: 'Azul',
          anio: 2024,
          clasificacion: 'Gasolina' as never,
          numeroPuertas: 4,
          capacidadMaletero: 470,
        },
      }),
    ).rejects.toThrow('Ya existe un vehiculo con esa placa');
  });
});
