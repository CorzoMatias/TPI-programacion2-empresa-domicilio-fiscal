package tpi.entities;

public class Empresa {
    private Long id;
    private boolean eliminado;
    private String razonSocial;       // NOT NULL (<=120)
    private String cuit;              // NOT NULL UNIQUE (<=13)
    private String actividadPrincipal;// (<=80)
    private String email;             // (<=120)
    // Relación 1→1 unidireccional: Empresa -> DomicilioFiscal
    private DomicilioFiscal domicilioFiscal;

    public Empresa() {}

    public Empresa(Long id, boolean eliminado, String razonSocial, String cuit,
                   String actividadPrincipal, String email, DomicilioFiscal domicilioFiscal) {
        this.id = id;
        this.eliminado = eliminado;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
        this.domicilioFiscal = domicilioFiscal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public String getActividadPrincipal() { return actividadPrincipal; }
    public void setActividadPrincipal(String actividadPrincipal) { this.actividadPrincipal = actividadPrincipal; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public DomicilioFiscal getDomicilioFiscal() { return domicilioFiscal; }
    public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) { this.domicilioFiscal = domicilioFiscal; }

    @Override
    public String toString() {
        String df = (domicilioFiscal == null) ? "null" : ("DomicilioFiscal{id=" + domicilioFiscal.getId() + "}");
        return "Empresa{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", actividadPrincipal='" + actividadPrincipal + '\'' +
                ", email='" + email + '\'' +
                ", domicilioFiscal=" + df +
                '}';
    }
}
