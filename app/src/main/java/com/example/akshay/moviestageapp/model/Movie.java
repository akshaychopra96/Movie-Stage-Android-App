package com.example.akshay.moviestageapp.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity(tableName = "movieTable")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @ColumnInfo(name = "rating")
    @SerializedName("vote_average")
    @Expose
    private Float voteAverage;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @ColumnInfo(name = "title")
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @ColumnInfo(name = "plotSynopsis")
    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @ColumnInfo(name = "favourite")
    private boolean isFavourite;

    /**
     *
     * @param id
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param backdropPath
     */
    public Movie( Integer id, Float voteAverage, String posterPath, String originalTitle, String backdropPath, String overview, String releaseDate, boolean isFavourite) {
        super();
        this.id = id;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.isFavourite = isFavourite;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean getIsFavourite() {return isFavourite;}

    public void setIsFavourite(boolean isFavourite){this.isFavourite = isFavourite;}

    public Movie(Parcel parcel){
        originalTitle = parcel.readString();
        voteAverage = parcel.readFloat();
        posterPath = parcel.readString();
        backdropPath = parcel.readString();
        releaseDate = parcel.readString();
        overview = parcel.readString();
        id = parcel.readInt();
        isFavourite = (parcel.readInt() ==0 )? false: true;
        /*
        *out.writeInt(isSelectionRight ? 1 : 0);
        read isSelectionRight  = (in.readInt() == 0) ? false : true;
         */

        }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(originalTitle);
        dest.writeFloat(voteAverage);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeInt(id);
        dest.writeInt(isFavourite? 1:0);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}