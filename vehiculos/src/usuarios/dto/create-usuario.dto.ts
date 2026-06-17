import { IsString, IsUUID, Matches, MaxLength, MinLength } from 'class-validator';

export class CreateUsuarioDto {
  @IsUUID()
  personId!: string;

  @IsString()
  @MinLength(8)
  @MaxLength(72)
  @Matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s])\S+$/, {
    message:
      'La contrasena debe incluir mayuscula, minuscula, numero, simbolo y no contener espacios',
  })
  password!: string;
}
