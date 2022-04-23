package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int codigoPais;
    private String nombrePais;
    private String capitalPais;
    private String region;
    private Long poblacion;
    private BigDecimal latitud;
    private BigDecimal longitud;

    public Pais() {
    }

    public Builder builder(){
        return new Builder();
    }

    public int getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(int codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getCapitalPais() {
        return capitalPais;
    }

    public void setCapitalPais(String capitalPais) {
        this.capitalPais = capitalPais;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Long poblacion) {
        this.poblacion = poblacion;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Pais{" +
                "codigoPais=" + codigoPais +
                ", nombrePais='" + nombrePais + '\'' +
                ", capitalPais='" + capitalPais + '\'' +
                ", region='" + region + '\'' +
                ", poblacion=" + poblacion +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }

    public static class Builder{
        private Pais pais;
        public Builder(){
            this.pais = new Pais();
        }

        public Builder codigo(int codigo){
            pais.setCodigoPais(codigo);
            return this;
        }
        public Builder nombre (String nombre){
            pais.setNombrePais(nombre);
            return this;
        }

        public Builder capital (String capital){
            pais.setCapitalPais(capital);
            return this;
        }

        public Builder region (String region){
            pais.setRegion(region);
            return this;
        }

        public Builder poblacion(Long poblacion){
            pais.setPoblacion(poblacion);
            return this;
        }

        public Builder latitud(BigDecimal latutud){
            pais.setLatitud(latutud);
            return this;
        }

        public Builder longitud(BigDecimal longitud){
            pais.setLongitud(longitud);
            return this;
        }

        public Pais build(){
            return this.pais;
        }
    }
}
