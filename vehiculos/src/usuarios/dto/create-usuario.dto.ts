import { IsString, IsUUID, Matches, MaxLength, MinLength } from 'class-validator';

export class CreateUsuarioDto {
  @IsUUID()
  personId!: string;

  @IsString()
  @MinLength(8)
  @MaxLength(72)
  @Matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/, {
    message: 'La contrasena debe incluir mayuscula, minuscula y numero',
  })
  password!: string;
}
