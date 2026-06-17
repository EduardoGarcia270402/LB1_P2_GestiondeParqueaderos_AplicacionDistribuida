import { IsUUID } from 'class-validator';

export class CreateTicketDto {
  @IsUUID()
  userId!: string;

  @IsUUID()
  vehiculoId!: string;

  @IsUUID()
  espacioId!: string;
}
