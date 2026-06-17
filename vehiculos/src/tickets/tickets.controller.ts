import { Body, Controller, Get, Param, ParseUUIDPipe, Patch, Post } from '@nestjs/common';
import { CerrarTicketDto } from './dto/cerrar-ticket.dto';
import { CreateTicketDto } from './dto/create-ticket.dto';
import { TicketsService } from './tickets.service';

@Controller('tickets')
export class TicketsController {
  constructor(private readonly ticketsService: TicketsService) {}

  @Post()
  crear(@Body() dto: CreateTicketDto) {
    return this.ticketsService.crear(dto);
  }

  @Get()
  findAll() {
    return this.ticketsService.findAll();
  }

  @Get(':id')
  findOne(@Param('id', ParseUUIDPipe) id: string) {
    return this.ticketsService.findOne(id);
  }

  @Patch(':id/cerrar')
  cerrar(
    @Param('id', ParseUUIDPipe) id: string,
    @Body() dto: CerrarTicketDto,
  ) {
    return this.ticketsService.cerrar(id, dto);
  }
}
