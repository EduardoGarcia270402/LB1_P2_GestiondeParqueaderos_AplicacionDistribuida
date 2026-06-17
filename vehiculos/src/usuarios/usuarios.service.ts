import {
  BadRequestException,
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import * as bcrypt from 'bcryptjs';
import { Like, Repository } from 'typeorm';
import { Persona } from '../personas/entities/persona.entity';
import { CreateUsuarioDto } from './dto/create-usuario.dto';
import { Usuario } from './entities/usuario.entity';

@Injectable()
export class UsuariosService {
  constructor(
    @InjectRepository(Usuario)
    private readonly usuariosRepository: Repository<Usuario>,
    @InjectRepository(Persona)
    private readonly personasRepository: Repository<Persona>,
  ) {}

  async create(dto: CreateUsuarioDto): Promise<Usuario> {
    const persona = await this.personasRepository.findOneBy({
      id: dto.personId,
      active: true,
    });
    if (!persona) {
      throw new NotFoundException('Persona activa no encontrada');
    }

    const usuarioPersona = await this.usuariosRepository.findOneBy({
      idPerson: dto.personId,
    });
    if (usuarioPersona) {
      throw new ConflictException('La persona ya tiene un usuario');
    }

    const username = await this.generarUsername(persona);

    const usuario = this.usuariosRepository.create({
      idPerson: dto.personId,
      persona,
      username,
      passwordHash: await bcrypt.hash(dto.password, 12),
    });
    await this.usuariosRepository.save(usuario);
    return this.findOne(dto.personId);
  }

  findAll(): Promise<Usuario[]> {
    return this.usuariosRepository.find({
      relations: {
        persona: true,
        usuarioRoles: { rol: true },
      },
      order: { createdAt: 'DESC' },
    });
  }

  async findOne(id: string): Promise<Usuario> {
    const usuario = await this.usuariosRepository.findOne({
      where: { idPerson: id },
      relations: {
        persona: true,
        usuarioRoles: { rol: true },
      },
    });
    if (!usuario) {
      throw new NotFoundException('Usuario no encontrado');
    }
    return usuario;
  }

  private async generarUsername(persona: Persona): Promise<string> {
    const primerNombre = this.normalizar(persona.firstName);
    const segundoNombre = this.normalizar(persona.middleName ?? '');
    const apellidoPaterno = this.normalizar(
      persona.lastName.trim().split(/\s+/)[0] ?? '',
    );

    if (!primerNombre || !segundoNombre || !apellidoPaterno) {
      throw new BadRequestException(
        'La persona debe tener primer nombre, segundo nombre y apellido paterno para generar el username',
      );
    }

    const base = `${primerNombre[0]}${segundoNombre[0]}${apellidoPaterno}`;
    const existentes = await this.usuariosRepository.find({
      select: { username: true },
      where: { username: Like(`${base}%`) },
    });
    const usados = new Set(existentes.map((usuario) => usuario.username));

    let secuencia = 1;
    let username = this.construirUsername(base, secuencia);
    while (usados.has(username)) {
      secuencia += 1;
      username = this.construirUsername(base, secuencia);
    }

    return username;
  }

  private construirUsername(base: string, secuencia: number): string {
    const suffix = String(secuencia);
    return `${base.slice(0, 15 - suffix.length)}${suffix}`;
  }

  private normalizar(value: string): string {
    return value
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .replace(/[^a-z0-9]/g, '');
  }
}
