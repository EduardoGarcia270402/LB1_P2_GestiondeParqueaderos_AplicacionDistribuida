import {
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CreateVehiculoDto } from './dto/create-vehiculo.dto';
import { UpdateVehiculoDto } from './dto/update-vehiculo.dto';
import { Vehiculo } from './entities/vehiculo.entity';
import { FactoryVehiculos } from './factory/factory-vehiculo';

@Injectable()
export class VehiculosService {
  constructor(
    @InjectRepository(Vehiculo)
    private readonly vehiculosRepository: Repository<Vehiculo>,
  ) {}

  async create(dto: CreateVehiculoDto): Promise<Vehiculo> {
    const existente = await this.vehiculosRepository.findOneBy({
      placa: dto.datos.placa,
    });
    if (existente) {
      throw new ConflictException('Ya existe un vehiculo con esa placa');
    }
    return this.vehiculosRepository.save(FactoryVehiculos.crear(dto));
  }

  findAll(): Promise<Vehiculo[]> {
    return this.vehiculosRepository.find();
  }

  async findOne(id: string): Promise<Vehiculo> {
    const vehiculo = await this.vehiculosRepository.findOneBy({ id });
    if (!vehiculo) {
      throw new NotFoundException('Vehiculo no encontrado');
    }
    return vehiculo;
  }

  async update(id: string, dto: UpdateVehiculoDto): Promise<Vehiculo> {
    const vehiculo = await this.findOne(id);
    if (dto.placa && dto.placa !== vehiculo.placa) {
      const placaOcupada = await this.vehiculosRepository.findOneBy({
        placa: dto.placa,
      });
      if (placaOcupada) {
        throw new ConflictException('Ya existe un vehiculo con esa placa');
      }
    }
    return this.vehiculosRepository.save(
      this.vehiculosRepository.merge(vehiculo, dto),
    );
  }

  async remove(id: string): Promise<void> {
    const vehiculo = await this.findOne(id);
    await this.vehiculosRepository.remove(vehiculo);
  }
}
