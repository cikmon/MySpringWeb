package com.app.cikmon.market.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Simple JavaBean domain object that represents a Product.
 *
 * @author cikmon
 * @version 1.0
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private Long id;
    private int version;
    private String name;
    private String manufacturer;
    private String productModel;
    private String description;
    private double price;
    private int availability;
    private byte[] photo;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    public Long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Version
    @Column(name = "VERSION")
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    @Column(name = "DESCRIPTION")
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


    @Column(name = "PRICE")
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @NotEmpty(message="{validation.lastname.NotEmpty.message}")
    @Size(min=2, max=80, message="{validation.lastname.Size.message}")
    @Column(name = "MANUFACTURER")
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    @Column(name = "PRODUCTMODEL")
    public String getProductModel() { return productModel; }
    public void setProductModel(String model) { this.productModel = model; }

    @Column(name = "AVAILABILITY")
    public int getAvailability() { return availability; }
    public void setAvailability(int availability) { this.availability = availability; }

    @NotEmpty(message="{validation.firstname.NotEmpty.message}")
    @Size(min=3, max=60, message="{validation.firstname.Size.message}")
    @Column(name = "NAME")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Basic(fetch= FetchType.LAZY)
    @Lob
    @Column(name = "PHOTO")
    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", descriptoin='" + description + '\'' +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + productModel + '\'' +
                ", availability=" + availability +
                ", name='" + name + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
