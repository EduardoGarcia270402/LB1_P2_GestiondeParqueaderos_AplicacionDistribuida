import { IsIn, IsOptional, IsString, MaxLength } from 'class-validator';

export class CreateRolDto {
  @IsIn(['CLIENTE', 'OPERADOR'])
  name!: 'CLIENTE' | 'OPERADOR';

  @IsOptional()
  @IsString()
  @MaxLength(255)
  description?: string;
}
