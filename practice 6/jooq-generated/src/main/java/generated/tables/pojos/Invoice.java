/*
 * This file is generated by jOOQ.
 */
package generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final LocalDate invoiceDate;
    private final Long organisationTin;

    public Invoice(Invoice value) {
        this.id = value.id;
        this.invoiceDate = value.invoiceDate;
        this.organisationTin = value.organisationTin;
    }

    public Invoice(
        Long id,
        LocalDate invoiceDate,
        Long organisationTin
    ) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.organisationTin = organisationTin;
    }

    /**
     * Getter for <code>public.invoice.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.invoice.invoice_date</code>.
     */
    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    /**
     * Getter for <code>public.invoice.organisation_tin</code>.
     */
    public Long getOrganisationTin() {
        return this.organisationTin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Invoice other = (Invoice) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.invoiceDate == null) {
            if (other.invoiceDate != null)
                return false;
        }
        else if (!this.invoiceDate.equals(other.invoiceDate))
            return false;
        if (this.organisationTin == null) {
            if (other.organisationTin != null)
                return false;
        }
        else if (!this.organisationTin.equals(other.organisationTin))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
        result = prime * result + ((this.organisationTin == null) ? 0 : this.organisationTin.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Invoice (");

        sb.append(id);
        sb.append(", ").append(invoiceDate);
        sb.append(", ").append(organisationTin);

        sb.append(")");
        return sb.toString();
    }
}