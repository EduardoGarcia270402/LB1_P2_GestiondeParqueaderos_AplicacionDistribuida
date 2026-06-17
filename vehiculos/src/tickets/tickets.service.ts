import {
  ConflictException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Usuario } from '../usuarios/entities/usuario.entity';
import { Vehiculo } from '../vehiculos/entities/vehiculo.entity';
import { CerrarTicketDto } from './dto/cerrar-ticket.dto';
import { CreateTicketDto } from './dto/create-ticket.dto';
import {
  EspacioTicket,
  EstadoEspacioTicket,
} from './entities/espacio-ticket.entity';
import { EstadoTicket, Ticket } from './entities/ticket.entity';

@Injectable()
export class TicketsService {
  constructor(
    @InjectRepository(Ticket)
    private readonly ticketsRepository: Repository<Ticket>,
    @InjectRepository(Usuario)
    private readonly usuariosRepository: Repository<Usuario>,
    @InjectRepository(Vehiculo)
    private readonly vehiculosRepository: Repository<Vehiculo>,
    @InjectRepository(EspacioTicket)
    private readonly espaciosRepository: Repository<EspacioTicket>,
  ) {}

  async crear(dto: CreateTicketDto): Promise<Ticket> {
    const [usuario, vehiculo, espacio] = await Promise.all([
      this.usuariosRepository.findOneBy({ idPerson: dto.userId, active: true }),
      this.vehiculosRepository.findOneBy({ id: dto.vehiculoId }),
      this.espaciosRepository.findOneBy({ id: dto.espacioId }),
    ]);

    if (!usuario) throw new NotFoundException('Usuario activo no encontrado');
    if (!vehiculo) throw new NotFoundException('Vehiculo no encontrado');
    if (!espacio) throw new NotFoundException('Espacio no encontrado');
    if (!espacio.activo || espacio.estado !== EstadoEspacioTicket.DISPONIBLE) {
      throw new ConflictException('El espacio no esta disponible');
    }

    const ticketAbierto = await this.ticketsRepository.findOne({
      where: [
        { idVehiculo: dto.vehiculoId, estado: EstadoTicket.ABIERTO },
        { idEspacio: dto.espacioId, estado: EstadoTicket.ABIERTO },
      ],
    });
    if (ticketAbierto) {
      throw new ConflictException(
        'Ya existe un ticket abierto para el vehiculo o espacio',
      );
    }

    espacio.activo = false;
    espacio.estado = EstadoEspacioTicket.OCUPADO;
    await this.espaciosRepository.save(espacio);

    const ticket = this.ticketsRepository.create({
      idUser: dto.userId,
      idVehiculo: dto.vehiculoId,
      idEspacio: dto.espacioId,
      usuario,
      vehiculo,
      espacio,
    });
    return this.ticketsRepository.save(ticket);
  }

  findAll(): Promise<Ticket[]> {
    return this.ticketsRepository.find({ order: { fechaIngreso: 'DESC' } });
  }

  async findOne(id: string): Promise<Ticket> {
    const ticket = await this.ticketsRepository.findOneBy({ id });
    if (!ticket) throw new NotFoundException('Ticket no encontrado');
    return ticket;
  }

  async cerrar(id: string, dto: CerrarTicketDto): Promise<Ticket> {
    const ticket = await this.findOne(id);
    if (ticket.estado === EstadoTicket.CERRADO) {
      throw new ConflictException('El ticket ya esta cerrado');
    }

    const espacio = await this.espaciosRepository.findOneBy({
      id: ticket.idEspacio,
    });
    if (!espacio) throw new NotFoundException('Espacio no encontrado');

    ticket.estado = EstadoTicket.CERRADO;
    ticket.fechaSalida = new Date();
    ticket.total = String(dto.total ?? 0);

    espacio.activo = true;
    espacio.estado = EstadoEspacioTicket.DISPONIBLE;

    await this.espaciosRepository.save(espacio);
    return this.ticketsRepository.save(ticket);
  }
}
