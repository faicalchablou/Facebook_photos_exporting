package ma.chablou.adria.model;


/**
 * Created by Faical chablou on 21/10/2018.
 */


import java.io.Serializable;

public class AlbumItem implements Serializable {
    private String idAlbum;
    private String nameAlbum;
    private String dateCreation;

    public AlbumItem() {

    }

    public AlbumItem(String idAlbum, String nameAlbum, String dateCreation) {
        this.idAlbum = idAlbum;
        this.nameAlbum = nameAlbum;
        this.dateCreation = dateCreation;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
}