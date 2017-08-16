package com.evenly.marcus_kopp.evenlyhq4square;

public class FoursquareVenue {

	// not used due to time constraints
    private int distance;

    private String name;

	private String address;

	private String city;

	private String category;

	public FoursquareVenue() {
		this.name = "";
		this.city = "";
		this.address = "";
        this.distance = 0;
		this.setCategory("");
	}

	public String getCity() {
		if (city.length() > 0) {
			return city;
		}
		return city;
	}

	public void setCity(String city) {
		if (city != null) {
			this.city = city.replaceAll("\\(", "").replaceAll("\\)", "");
			;
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
        return address;
    }

	public void setName(String name) {
        this.name = name;
	}

	public String getName() {
        return name;
	}

	public String getCategory() {
        return category;
	}

	public void setCategory(String category) {
        this.category = category;
	}

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
