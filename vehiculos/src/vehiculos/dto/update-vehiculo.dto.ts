import { PartialType } from '@nestjs/mapped-types';
import { AutoDto } from './create-vehiculo.dto';
import { TipoMoto } from '../entities/motocicleta.entity';
import { IsEnum, IsIn, IsOptional, IsString, Matches } from 'class-validator';

export class UpdateVehiculoDto extends PartialType(AutoDto) {
  @IsOptional()
  @IsIn([2, 4])
  cabina?: number;

  @IsOptional()
  @IsString()
  @Matches(/^[0-9]+(\.[0-9]+)?\s?(kg|KG|t|T)$/)
  capacidadCarga?: string;

  @IsOptional()
  @IsEnum(TipoMoto)
  tipoMoto?: TipoMoto;
}
