import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryColumn,
  UpdateDateColumn,
} from 'typeorm';
import { Usuario } from '../../usuarios/entities/usuario.entity';
import { Rol } from './rol.entity';

@Entity({ name: 'user_role' })
export class UsuarioRol {
  @PrimaryColumn('uuid', { name: 'id_user' })
  idUser!: string;

  @PrimaryColumn('uuid', { name: 'id_role' })
  idRole!: string;

  @ManyToOne(() => Usuario, (usuario) => usuario.usuarioRoles, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'id_user', referencedColumnName: 'idPerson' })
  usuario!: Usuario;

  @ManyToOne(() => Rol, (rol) => rol.usuarioRoles, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'id_role' })
  rol!: Rol;

  @Column({ default: true })
  active!: boolean;

  @CreateDateColumn({ name: 'assigned_at', type: 'timestamp' })
  assignedAt!: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamp' })
  updatedAt!: Date;
}
