import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  OneToMany,
  OneToOne,
  PrimaryColumn,
  UpdateDateColumn,
} from 'typeorm';
import { Persona } from '../../personas/entities/persona.entity';
import { UsuarioRol } from '../../roles/entities/usuario-rol.entity';

@Entity({ name: 'users' })
export class Usuario {
  @PrimaryColumn('uuid', { name: 'id_person' })
  idPerson!: string;

  @OneToOne(() => Persona, (persona) => persona.usuario, {
    onDelete: 'CASCADE',
  })
  @JoinColumn({ name: 'id_person' })
  persona!: Persona;

  @Column({ unique: true, length: 15 })
  username!: string;

  @Column({ name: 'password_hash', length: 255, select: false })
  passwordHash!: string;

  @Column({ default: true })
  active!: boolean;

  @Column({ name: 'last_login', type: 'timestamp', nullable: true })
  lastLogin?: Date;

  @CreateDateColumn({ name: 'created_at', type: 'timestamp' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at', type: 'timestamp' })
  updatedAt!: Date;

  @OneToMany(() => UsuarioRol, (usuarioRol) => usuarioRol.usuario)
  usuarioRoles?: UsuarioRol[];
}
