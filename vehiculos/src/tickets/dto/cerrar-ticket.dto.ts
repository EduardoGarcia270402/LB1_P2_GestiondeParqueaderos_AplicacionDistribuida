import { IsNumber, IsOptional, Min } from 'class-validator';

export class CerrarTicketDto {
  @IsOptional()
  @IsNumber()
  @Min(0)
  total?: number;
}
