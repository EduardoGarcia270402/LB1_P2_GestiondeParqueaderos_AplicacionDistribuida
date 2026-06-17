import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

export enum EstadoEspacioTicket {
  DISPONIBLE = 'DISPONIBLE',
  OCUPADO = 'OCUPADO',
  RESERVADO = 'RESERVADO',
  MANTENIMIENTO = 'MANTENIMIENTO',
}

@Entity({ name: 'espacios', synchronize: false })
export class EspacioTicket {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ length: 32 })
  codigo!: string;

  @Column({ default: true })
  activo!: boolean;

  @Column({ type: 'varchar', length: 20 })
  estado!: EstadoEspacioTicket;
}
