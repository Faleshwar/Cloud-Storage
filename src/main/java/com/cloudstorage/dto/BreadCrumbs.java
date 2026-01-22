package com.cloudstorage.dto;

import java.util.Objects;
import java.util.UUID;

public class BreadCrumbs {
	private String name;
	private UUID id;
	public BreadCrumbs() {
		
	}
	public BreadCrumbs(String name, UUID id) {
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BreadCrumbs other = (BreadCrumbs) obj;
		return Objects.equals(name, other.name);
	}
	
}
