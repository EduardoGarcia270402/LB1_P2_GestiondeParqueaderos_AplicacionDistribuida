import { CreateVehiculoDto } from '../dto/create-vehiculo.dto';
import { Auto } from '../entities/auto.entity';
import { Camioneta } from '../entities/camioneta.entity';
import { Motocicleta } from '../entities/motocicleta.entity';
import { Vehiculo } from '../entities/vehiculo.entity';

export class FactoryVehiculos {
  static crear(dto: CreateVehiculoDto): Vehiculo {
    let vehiculo: Vehiculo;

    switch (dto.tipo) {
      case 'Auto':
        vehiculo = new Auto();
        break;
      case 'Motocicleta':
        vehiculo = new Motocicleta();
        break;
      case 'Camioneta':
        vehiculo = new Camioneta();
        break;
      default:
        throw new Error(`Tipo de vehiculo no soportado: ${String(dto.tipo)}`);
    }

    return Object.assign(vehiculo, dto.datos);
  }
}
