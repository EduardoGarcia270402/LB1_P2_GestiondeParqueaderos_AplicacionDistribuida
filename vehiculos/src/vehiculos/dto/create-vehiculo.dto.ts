import { Type } from 'class-transformer';
import {
  IsEnum,
  IsIn,
  IsInt,
  IsNotEmpty,
  IsString,
  Matches,
  Max,
  MaxLength,
  Min,
  MinLength,
  ValidateNested,
} from 'class-validator';
import { Clasificacion } from '../entities/vehiculo.entity';
import { TipoMoto } from '../entities/motocicleta.entity';

const ANIO_MIN = 1886;
const ANIO_MAX = new Date().getFullYear() + 1;

export class BaseVehiculoDto {
  @IsString()
  @Matches(/^[A-Z0-9-]{6,10}$/, {
    message: 'La placa debe tener entre 6 y 10 caracteres en mayusculas',
  })
  placa!: string;

  @IsString()
  @IsNotEmpty()
  @MinLength(2)
  @MaxLength(50)
  marca!: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(100)
  modelo!: string;

  @IsString()
  @IsNotEmpty()
  @MaxLength(50)
  color!: string;

  @IsInt()
  @Min(ANIO_MIN)
  @Max(ANIO_MAX)
  anio!: number;

  @IsEnum(Clasificacion)
  clasificacion!: Clasificacion;
}

export class AutoDto extends BaseVehiculoDto {
  @IsInt()
  @Min(2)
  @Max(5)
  numeroPuertas!: number;

  @IsInt()
  @Min(50)
  @Max(1500)
  capacidadMaletero!: number;
}

export class MotocicletaDto extends BaseVehiculoDto {
  @IsEnum(TipoMoto)
  tipoMoto!: TipoMoto;
}

export class CamionetaDto extends BaseVehiculoDto {
  @IsIn([2, 4])
  cabina!: number;

  @IsString()
  @Matches(/^[0-9]+(\.[0-9]+)?\s?(kg|KG|t|T)$/)
  capacidadCarga!: string;
}

export class CreateVehiculoDto {
  @IsIn(['Auto', 'Motocicleta', 'Camioneta'])
  tipo!: 'Auto' | 'Motocicleta' | 'Camioneta';

  @ValidateNested()
  @Type((options) => {
    const object = options?.object as CreateVehiculoDto | undefined;
    switch (object?.tipo) {
      case 'Auto':
        return AutoDto;
      case 'Motocicleta':
        return MotocicletaDto;
      case 'Camioneta':
        return CamionetaDto;
      default:
        return BaseVehiculoDto;
    }
  })
  datos!: AutoDto | MotocicletaDto | CamionetaDto;
}
