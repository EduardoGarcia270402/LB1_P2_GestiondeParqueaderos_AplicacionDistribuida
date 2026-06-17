import {
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Not, Repository } from 'typeorm';
import { CreatePersonaDto } from './dto/create-persona.dto';
import { UpdatePersonaDto } from './dto/update-persona.dto';
import { Persona } from './entities/persona.entity';

@Injectable()
export class PersonasService {
  constructor(
    @InjectRepository(Persona)
    private readonly personasRepository: Repository<Persona>,
  ) {}

  async create(dto: CreatePersonaDto): Promise<Persona> {
    await this.validarUnicos(dto);
    return this.personasRepository.save(this.personasRepository.create(dto));
  }

  findAll(): Promise<Persona[]> {
    return this.personasRepository.find({ order: { createdAt: 'DESC' } });
  }

  async findOne(id: string): Promise<Persona> {
    const persona = await this.personasRepository.findOne({
      where: { id },
      relations: { usuario: true },
    });
    if (!persona) {
      throw new NotFoundException('Persona no encontrada');
    }
    return persona;
  }

  async update(id: string, dto: UpdatePersonaDto): Promise<Persona> {
    const persona = await this.findOne(id);
    await this.validarUnicos(dto, id);
    return this.personasRepository.save(
      this.personasRepository.merge(persona, dto),
    );
  }

  async remove(id: string): Promise<void> {
    const persona = await this.findOne(id);
    persona.active = false;
    await this.personasRepository.save(persona);
  }

  private async validarUnicos(
    dto: Partial<CreatePersonaDto>,
    id?: string,
  ): Promise<void> {
    const condicionId = id ? Not(id) : undefined;
    const consultas = [
      dto.dni
        ? this.personasRepository.findOneBy({ dni: dto.dni, id: condicionId })
        : null,
      dto.email
        ? this.personasRepository.findOneBy({
            email: dto.email,
            id: condicionId,
          })
        : null,
      dto.phone
        ? this.personasRepository.findOneBy({
            phone: dto.phone,
            id: condicionId,
          })
        : null,
    ].filter((consulta) => consulta !== null);

    const duplicados = await Promise.all(consultas);
    if (duplicados.some(Boolean)) {
      throw new ConflictException(
        'Ya existe una persona con el mismo DNI, email o telefono',
      );
    }
  }
}
