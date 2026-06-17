import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Usuario } from '../usuarios/entities/usuario.entity';
import { Vehiculo } from '../vehiculos/entities/vehiculo.entity';
import { EspacioTicket } from './entities/espacio-ticket.entity';
import { Ticket } from './entities/ticket.entity';
import { TicketsController } from './tickets.controller';
import { TicketsService } from './tickets.service';

@Module({
  imports: [
    TypeOrmModule.forFeature([Ticket, Usuario, Vehiculo, EspacioTicket]),
  ],
  controllers: [TicketsController],
  providers: [TicketsService],
})
export class TicketsModule {}
