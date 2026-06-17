import {
  IsEmail,
  IsNotEmpty,
  IsOptional,
  IsString,
  Matches,
  MaxLength,
  MinLength,
} from 'class-validator';

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
  firstName!: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  middleName?: string;

  @IsString()
  @IsNotEmpty()
  @MinLength(2)
  @MaxLength(30)
  lastName!: string;

  @IsEmail()
  @MaxLength(50)
  email!: string;

  @IsString()
  @Matches(/^\d{7,15}$/, {
    message: 'El telefono debe contener entre 7 y 15 digitos',
  })
  phone!: string;

  @IsOptional()
  @IsString()
  address?: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  nationality?: string;
}
