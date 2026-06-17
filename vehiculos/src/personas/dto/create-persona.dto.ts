import {
  IsEmail,
  IsNotEmpty,
  IsOptional,
  IsString,
  Matches,
  MaxLength,
  MinLength,
} from 'class-validator';

const NAME_PATTERN = /^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?: [A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$/;

export class CreatePersonaDto {
  @IsString()
  @Matches(/^\d{10,13}$/, {
    message: 'El DNI debe contener entre 10 y 13 digitos',
  })
  dni!: string;

  @IsString()
  @IsNotEmpty()
  @MinLength(2)
  @MaxLength(30)
  @Matches(NAME_PATTERN, {
    message: 'El primer nombre solo debe contener letras y espacios simples',
  })
  firstName!: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  @Matches(NAME_PATTERN, {
    message: 'El segundo nombre solo debe contener letras y espacios simples',
  })
  middleName?: string;

  @IsString()
  @IsNotEmpty()
  @MinLength(2)
  @MaxLength(30)
  @Matches(NAME_PATTERN, {
    message: 'El apellido solo debe contener letras y espacios simples',
  })
  lastName!: string;

  @IsEmail()
  @MaxLength(50)
  email!: string;

  @IsString()
  @Matches(/^09\d{8}$/, {
    message: 'El telefono debe ser un celular ecuatoriano de 10 digitos',
  })
  phone!: string;

  @IsOptional()
  @IsString()
  address?: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  @Matches(NAME_PATTERN, {
    message: 'La nacionalidad solo debe contener letras y espacios simples',
  })
  nationality?: string;
}
