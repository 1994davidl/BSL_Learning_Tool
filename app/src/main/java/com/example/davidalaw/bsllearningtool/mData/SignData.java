package com.example.davidalaw.bsllearningtool.mData;

/**
 * Constructers, setters & getters for SignData object
 *
 * Created by DavidALaw on 18/07/2017.
 */
public class SignData {

    //Instance variables
    private int id;
    private String categoryName, signName, bSLOrder;
    private String signSynonym, signOccurs, signShape, signConfig, signExpress, video_file_path;
    private int favourite, region_fk;

    public SignData() {
    }

    public SignData(String categoryName, String signName, String bSLOrder, String signSynonym,
                    String signOccurs, String signShape, String signConfig, String signExpress,
                    String video_file_path, int favourite, int region_fk) {
        this.categoryName = categoryName;
        this.signName = signName;
        this.bSLOrder = bSLOrder;
        this.signSynonym = signSynonym;
        this.signOccurs = signOccurs;
        this.signShape = signShape;
        this.signConfig = signConfig;
        this.signExpress = signExpress;
        this.video_file_path = video_file_path;
        this.favourite = favourite;
        this.region_fk = region_fk;
    }

    /**
     * Contstructor that tokenises the string line into
     * 11 different variables using a semi colon delimiter.
     *
     * @param line
     */
    public SignData(String line) {
        String[] tokens = line.split(";");

        categoryName = tokens[0];
        signName = tokens[1];
        bSLOrder = tokens[2];
        signSynonym = tokens[3];
        signOccurs = tokens[4];
        signShape = tokens[5];
        signConfig = tokens[6];
        signExpress = tokens[7];
        video_file_path = tokens[8];
        favourite = Integer.parseInt(tokens[9]);
        region_fk = Integer.parseInt(tokens[10]);
    }

    /**
     * @return the id of a sign
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id of sign to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the category name of sign to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the sign name
     */
    public String getSignName() {
        return signName;
    }

    /**
     * @param signName the sign name to set
     */
    public void setSignName(String signName) {
        this.signName = signName;
    }

    /**
     * @return the BSL Gloss
     */
    public String getbSLOrder() {
        return bSLOrder;
    }

    /**
     * @param bSLOrder the BSL gloss to set
     */
    public void setbSLOrder(String bSLOrder) {
        this.bSLOrder = bSLOrder;
    }

    /**
     * @return the sign synonym i.e what the sign also means (multiple meanings)
     */
    public String getSignSynonym() {
        return signSynonym;
    }

    /**
     * @param signSynonym the sign synonym to set
     */
    public void setSignSynonym(String signSynonym) {
        this.signSynonym = signSynonym;
    }

    /**
     * @return where the sign occurs on the body
     */
    public String getSignOccurs() {
        return signOccurs;
    }

    /**
     * @param signOccurs the sign occurs to set
     */
    public void setSignOccurs(String signOccurs) {
        this.signOccurs = signOccurs;
    }

    /**
     * @return the handshape of the sign
     */
    public String getSignShape() {
        return signShape;
    }

    /**
     * @param signShape the handshape of sign to set
     */
    public void setSignShape(String signShape) {
        this.signShape = signShape;
    }

    /**
     * @return the sign movement that occurs
     */
    public String getSignConfig() {
        return signConfig;
    }

    /**
     * @param signConfig the sign configuration to set
     */
    public void setSignConfig(String signConfig) {
        this.signConfig = signConfig;
    }

    /**
     * @return the sign facial expression.
     */
    public String getSignExpress() {
        return signExpress;
    }

    /**
     * @param signExpress the sign facial expression to set
     */
    public void setSignExpress(String signExpress) {
        this.signExpress = signExpress;
    }

    /**
     * @return the video demonstration http url.
     */
    public String getVideo_file_path() {
        return video_file_path;
    }

    /**
     * @param video_file_path video demonstration http url to set
     */
    public void setVideo_file_path(String video_file_path) {
        this.video_file_path = video_file_path;
    }

    /**
     * @return whether the sign has been favourited either 0 or 1
     */
    public int getFavourite() {
        return favourite;
    }

    /**
     * @param favourite set the favourite
     */
    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    /**
     * @return what region the sign is used in. Foreign key
     */
    public int getRegion_fk() {
        return region_fk;
    }

    /**
     * @param region_fk set the region foreign key.
     */
    public void setRegion_fk(int region_fk) {
        this.region_fk = region_fk;
    }
}
