/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 礼品信息
 * 
 * @author nym
 * 
 */
public class ProductInfo extends Entity implements Parcelable {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
        readObject(dest,this);
	}

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
		@Override
		public ProductInfo createFromParcel(Parcel source) {
			ProductInfo mMember = new ProductInfo();
            writeObject(source, mMember);
			return mMember;
		}

		@Override
		public ProductInfo[] newArray(int size) {
			return new ProductInfo[size];
		}
	};



    private String rec_id;	//购物车id
	private String isBest;
	private String isNew;
	private String isHot;
	private String isPromote;
	private String saleNumber;
	private String shopPrice;
	private String marketPrice;
	private String name;
	private String goodsId;
	private String img;
	private String pictures;
	private String suppliers;
	private String promotePrice;
	private String collected;

	public String getRec_id() {
		return rec_id;
	}

	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}

	public String getIsBest() {
		return isBest;
	}

	public void setIsBest(String isBest) {
		this.isBest = isBest;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getIsPromote() {
		return isPromote;
	}

	public void setIsPromote(String isPromote) {
		this.isPromote = isPromote;
	}

	public String getSaleNumber() {
		return saleNumber;
	}

	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}

	public String getShopPrice() {
		return shopPrice;
	}

	public void setShopPrice(String shopPrice) {
		this.shopPrice = shopPrice;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(String suppliers) {
		this.suppliers = suppliers;
	}

	public String getPromotePrice() {
		return promotePrice;
	}

	public void setPromotePrice(String promotePrice) {
		this.promotePrice = promotePrice;
	}

	public String getCollected() {
		return collected;
	}

	public void setCollected(String collected) {
		this.collected = collected;
	}


}
