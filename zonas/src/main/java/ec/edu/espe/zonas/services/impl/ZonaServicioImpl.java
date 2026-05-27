package ec.edu.espe.zonas.services.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.espe.zonas.dtos.ZonaRequestDto;
import ec.edu.espe.zonas.dtos.ZonaRespondeDto;
import ec.edu.espe.zonas.repositorios.ZonaRepositorio;
import ec.edu.espe.zonas.services.ZonaServicio;
import ec.edu.espe.zonas.entidades.Zona;



@Service
public class ZonaServicioImpl implements ZonaServicio {

    @Autowired
    private ZonaRepositorio repositorioZona;

    @Override
    public List<ZonaRespondeDto> listarZonas() {
        RepositorioZona.findAll(){
            return repositorioZona.findAll().stream()
            .map(this::mapToDto)
            .toList();
        }
    }

    @Override
    public ZonaRespondeDto crearZona(ZonaRequestDto request) {

        if(repositorioZona.existsByNombre(request.getNombre())){
            throw new RuntimeException("Ya existe una zona con el nombre: " + request.getNombre());
        }


        Zona objZona = new Zona();

        objZona.setNombre(request.getNombre());
        objZona.setCodigo(generarCodigo(request));
        objZona.setDescripcion(request.getDescripcion());
        objZona.setTipoZona(request.getTipoZona());

        repositorioZona.save(objZona);
        return mapToDto(objZona);
    }

    @Override
    public ZonaRespondeDto actualizarZona(UUID idZona, ZonaRequestDto request) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizarZona'");
    }

    @Override
    public void activarZona(UUID idZona) {
        throw new UnsupportedOperationException("Unimplemented method 'activarZona'");
    }


    private ZonaRespondeDto mapToDto(Zona objZona){
        return ZonaRespondeDto.builder()
        .idZona(objZona.getIdZona())
        .nombre(objZona.getNombre())
        .codigo(objZona.getCodigo())
        .descripcion(objZona.getDescripcion())
        .estado(objZona.getEstado())
        .tipoZona(objZona.getTipoZona())
        .espacios(objZona.getEspacios())
        .fechaCreacion(objZona.getFechaCreacion())
        .fechaActualizacion(objZona.getFechaActualizacion())
        .build();   
        // como mapeo los espacios?
        // 
    }

    private String generarCodigo(ZonaRequestDto request){
        return "ZONA-"+UUID.randomUUID().toString().substring(0, 8).toUpperCase(); //zona tip numero  ej. ZONA-REG-01

     
        //crear un clase con un archiov main importar el servicio y probar la funcion generarCodigo() para ver el resultado
        // bdd luego crear un packete de test, una clase con un metodo prinicpal main, dentro se crea una instancia de este sercicio y de ahí se teste el codigo
    }
    
}//crear base de datos usario contraseña y verificar si funciona correctamente el codigo 
//crear la kprimero zona, debe salir 01, porque no se inserta nada y la bdd esta vacia