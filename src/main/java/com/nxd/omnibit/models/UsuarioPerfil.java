package com.nxd.omnibit.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_perfil")
public class UsuarioPerfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_uuid", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private String authUuid; // UUID proveniente do Auctoritas

    @ManyToOne
    @JoinColumn(name = "cargo_uuid", referencedColumnName = "uuid", nullable = false)
    private Cargo cargo;

    @Deprecated
    public UsuarioPerfil() {
    }

    public UsuarioPerfil(String authUuid, Cargo cargo) {
        this.authUuid = authUuid;
        this.cargo = cargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthUuid() {
        return authUuid;
    }

    public void setAuthUuid(String authUuid) {
        this.authUuid = authUuid;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "UsuarioPerfil [id=" + id + ", authUuid=" + authUuid + ", cargo=" + cargo + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(authUuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UsuarioPerfil other = (UsuarioPerfil) obj;
        return Objects.equals(authUuid, other.authUuid);
    }           
    
}
