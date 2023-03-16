package lt.viko.eif.nlavkart.internetshop.models;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;

/**
 * Account class.
 */
@XmlType(name = "accounts", propOrder = {"id", "username", "password", "cart"})
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "account")
public class Account {
    /**
     * ID of the account.
     */
    @XmlAttribute(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * Username of the account.
     */
    private String username;
    /**
     * Password of the account.
     */
    private String password;
    /**
     * Cart of the account.
     */
    @OneToOne(targetEntity = Cart.class, cascade = CascadeType.ALL)
    private Cart cart;

    /**
     * No-Args constructor.
     */
    public Account() {
    }

    /**
     * Constructor with parameters.
     *
     * @param id       id
     * @param username username
     * @param password password
     * @param cart     cart
     */

    public Account(int id, String username, String password, Cart cart) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cart = cart;
    }

    /**
     * Get id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Set id.
     *
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username.
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password.
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get cart.
     *
     * @return cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Set cart.
     *
     * @param cart cart
     */
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * Overridden toString method.
     *
     * @return output
     */
    @Override
    public String toString() {
        String output;
        output = "ID: " + id + "\n";
        output += "Username: " + username + "\n";
        output += "Password: " + password + "\n";
        output += "\t--Cart--\n";
        output += cart;
        output += "\t--/Cart-\n";
        return output;
    }
}
