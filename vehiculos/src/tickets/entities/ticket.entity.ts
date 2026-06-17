import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { Usuario } from '../../usuarios/entities/usuario.entity';
import { Vehiculo } from '../../vehiculos/entities/vehiculo.entity';
import { EspacioTicket } from './espacio-ticket.entity';

export enum EstadoTicket {
  ABIERTO = 'ABIERTO',
  CERRADO = 'CERRADO',
}

@Entity({ name: 'tickets' })
export class Ticket {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ name: 'id_user', type: 'uuid' })
  idUser!: string;

  @ManyToOne(() => Usuario, { eager: true })
  @JoinColumn({ name: 'id_user', referencedColumnName: 'idPerson' })
  usuario!: Usuario;

  @Column({ name: 'id_vehiculo', type: 'uuid' })
  idVehiculo!: string;

  @ManyToOne(() => Vehiculo, { eager: true })
  @JoinColumn({ name: 'id_vehiculo' })
  vehiculo!: Vehiculo;

  @Column({ name: 'id_espacio', type: 'uuid' })
  idEspacio!: string;

  @ManyToOne(() => EspacioTicket, { eager: true })
  @JoinColumn({ name: 'id_espacio' })
  espacio!: EspacioTicket;

  @Column({ type: 'varchar', length: 20, default: EstadoTicket.ABIERTO })
  estado!: EstadoTicket;

  @CreateDateColumn({ name: 'fecha_ingreso', type: 'timestamp' })
  fechaIngreso!: Date;

  @Column({ name: 'fecha_salida', type: 'timestamp', nullable: true })
  fechaSalida?: Date;

  @Column({ type: 'numeric', precision: 10, scale: 2, default: 0 })
  total!: string;

  @CreateDateColumn({ name: 'created_at', type: 'timestamp' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamp' })
  updatedAt!: Date;
}
