package com.example.davidalaw.bsllearningtool.mSQLiteHandler;

/**
 * Created by DavidALaw on 18/07/2017.
 */
public class SignData {

    //Instance variables
    private int id;
    private String categoryName, signName, bSLOrder;
    private String signSynonym, signOccurs, signShape, signConfig, signExpress, video_file_path;
    private int favourite;

    public SignData() {

    }

    public SignData(int id, String categoryName, String signName, String bSLOrder, String signSynonym,
                    String signOccurs, String signShape, String signConfig, String signExpress,
                    String video_file_path, int favourite) {
        this.id = id;
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
    }

    public SignData(String categoryName, String signName, String bSLOrder, String signSynonym,
                    String signOccurs, String signShape, String signConfig,
                    String signExpress, String video_file_path, int favourite) {
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
    }

    public SignData(String line) {
        String [] tokens = line.split(";");

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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getbSLOrder() {
        return bSLOrder;
    }

    public void setbSLOrder(String bSLOrder) {
        this.bSLOrder = bSLOrder;
    }

    public String getSignSynonym() {
        return signSynonym;
    }

    public void setSignSynonym(String signSynonym) {
        this.signSynonym = signSynonym;
    }

    public String getSignOccurs() {
        return signOccurs;
    }

    public void setSignOccurs(String signOccurs) {
        this.signOccurs = signOccurs;
    }

    public String getSignShape() {
        return signShape;
    }

    public void setSignShape(String signShape) {
        this.signShape = signShape;
    }

    public String getSignConfig() {
        return signConfig;
    }

    public void setSignConfig(String signConfig) {
        this.signConfig = signConfig;
    }

    public String getSignExpress() {
        return signExpress;
    }

    public void setSignExpress(String signExpress) {
        this.signExpress = signExpress;
    }

    public String getVideo_file_path() {
        return video_file_path;
    }

    public void setVideo_file_path(String video_file_path) {
        this.video_file_path = video_file_path;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
