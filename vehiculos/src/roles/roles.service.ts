import {
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Usuario } from '../usuarios/entities/usuario.entity';
import { CreateRolDto } from './dto/create-rol.dto';
import { Rol } from './entities/rol.entity';
import { UsuarioRol } from './entities/usuario-rol.entity';

@Injectable()
export class RolesService {
  constructor(
    @InjectRepository(Rol)
    private readonly rolesRepository: Repository<Rol>,
    @InjectRepository(UsuarioRol)
    private readonly usuarioRolRepository: Repository<UsuarioRol>,
    @InjectRepository(Usuario)
    private readonly usuariosRepository: Repository<Usuario>,
  ) {}

  async create(dto: CreateRolDto): Promise<Rol> {
    const existente = await this.rolesRepository.findOneBy({ name: dto.name });
    if (existente) {
      throw new ConflictException('El rol ya existe');
    }
    return this.rolesRepository.save(this.rolesRepository.create(dto));
  }

  findAll(): Promise<Rol[]> {
    return this.rolesRepository.find({ order: { name: 'ASC' } });
  }

  async asignarRol(userId: string, roleId: string): Promise<UsuarioRol> {
    const [usuario, rol, asignacion] = await Promise.all([
      this.usuariosRepository.findOneBy({ idPerson: userId, active: true }),
      this.rolesRepository.findOneBy({ id: roleId, active: true }),
      this.usuarioRolRepository.findOneBy({
        idUser: userId,
        idRole: roleId,
      }),
    ]);
    if (!usuario) {
      throw new NotFoundException('Usuario activo no encontrado');
    }
    if (!rol) {
      throw new NotFoundException('Rol activo no encontrado');
    }
    if (asignacion) {
      throw new ConflictException('El usuario ya tiene asignado este rol');
    }

    return this.usuarioRolRepository.save(
      this.usuarioRolRepository.create({
        idUser: userId,
        idRole: roleId,
        usuario,
        rol,
      }),
    );
  }

  async listarRolesDeUsuario(userId: string): Promise<Rol[]> {
    const usuario = await this.usuariosRepository.findOneBy({
      idPerson: userId,
    });
    if (!usuario) {
      throw new NotFoundException('Usuario no encontrado');
    }

    const asignaciones = await this.usuarioRolRepository.find({
      where: { idUser: userId, active: true },
      relations: { rol: true },
      order: { assignedAt: 'ASC' },
    });
    return asignaciones.map((asignacion) => asignacion.rol);
  }
}
