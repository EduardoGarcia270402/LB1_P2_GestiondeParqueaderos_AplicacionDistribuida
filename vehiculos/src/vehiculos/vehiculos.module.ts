import { Module } from '@nestjs/common';
import { VehiculosService } from './vehiculos.service';
import { VehiculosController } from './vehiculos.controller';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Vehiculo } from './entities/vehiculo.entity';
import { Auto } from './entities/auto.entity';
import { Camioneta } from './entities/camioneta.entity';
import { Motocicleta } from './entities/motocicleta.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Vehiculo, Auto, Camioneta, Motocicleta])],
  controllers: [VehiculosController],
  providers: [VehiculosService],
})
export class VehiculosModule {}
