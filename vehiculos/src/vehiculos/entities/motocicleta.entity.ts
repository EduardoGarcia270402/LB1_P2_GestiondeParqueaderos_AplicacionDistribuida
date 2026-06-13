import { ChildEntity, Column } from 'typeorm';
import { Vehiculo } from './vehiculo.entity';

export enum TipoMoto {
    DEPORTIVA = 'Deportiva',
    SCOOTER = 'Scooter',
    MOTOCROSS = 'Motocross'
}

@ChildEntity('Motocicleta')
export class Motocicleta extends Vehiculo {
  @Column({ type: 'enum', enum: TipoMoto, name: 'tipo_moto' })
  tipoMoto!: TipoMoto;
}
