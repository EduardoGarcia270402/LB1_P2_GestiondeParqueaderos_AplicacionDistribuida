import {
  Column,
  Entity,
  PrimaryGeneratedColumn,
  TableInheritance,
} from 'typeorm';

export enum Clasificacion {
  ELECTRICO = 'Electrico',
  HIBRIDO = 'Hibrido',
  GASOLINA = 'Gasolina',
  DIESEL = 'Diesel',
}

@Entity({ name: 'vehiculos' })
@TableInheritance({ column: { type: 'varchar', name: 'tipo' } })
export abstract class Vehiculo {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true, length: 16 })
  placa!: string;

  @Column({ length: 50 })
  marca!: string;

  @Column({ length: 100 })
  modelo!: string;

  @Column({ length: 50 })
  color!: string;

  @Column()
  anio!: number;

  @Column({ type: 'enum', enum: Clasificacion })
  clasificacion!: Clasificacion;
}

