export class ResponseVehiculoDto {
  id!: string;
  placa!: string;
  marca!: string;
  modelo!: string;
  color!: string;
  anio!: number;
  clasificacion!: string;
  tipo!: string;
  numeroPuertas?: number;
  capacidadMaletero?: number;
  cabina?: number;
  capacidadCarga?: string;
  tipoMoto?: string;
}
