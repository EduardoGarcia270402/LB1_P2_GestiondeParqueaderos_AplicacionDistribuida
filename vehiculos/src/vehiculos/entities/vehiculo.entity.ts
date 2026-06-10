import { Column, PrimaryGeneratedColumn } from "typeorm";

export class Vehiculo {

    @PrimaryGeneratedColumn('uuid')
    id: string;

    @Column({unique:true})
    placa!: string;

    @Column()
    marca!:string;

    @Column()
    modelo!: string;

    @Column()
    color!: string;

    @Column()
    anio!: number;
}

