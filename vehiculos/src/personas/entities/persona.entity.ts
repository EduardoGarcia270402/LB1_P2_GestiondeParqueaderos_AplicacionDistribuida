import {
  Column,
  CreateDateColumn,
  Entity,
  OneToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn,
} from 'typeorm';
import { Usuario } from '../../usuarios/entities/usuario.entity';

@Entity({ name: 'persons' })
export class Persona {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ name: 'dni', unique: true, length: 30 })
  dni!: string;

  @Column({ name: 'first_name', length: 30 })
  firstName!: string;

  @Column({ name: 'middle_name', length: 30, nullable: true })
  middleName?: string;

  @Column({ name: 'last_name', length: 30 })
  lastName!: string;

  @Column({ unique: true, length: 50 })
  email!: string;

  @Column({ unique: true, length: 15 })
  phone!: string;

  @Column({ type: 'text', nullable: true })
  address?: string;

  @Column({ length: 30, nullable: true })
  nationality?: string;

  @Column({ default: true })
  active!: boolean;

  @CreateDateColumn({ name: 'created_at', type: 'timestamp' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamp' })
  updatedAt!: Date;

  @OneToOne(() => Usuario, (usuario) => usuario.persona)
  usuario?: Usuario;
}
