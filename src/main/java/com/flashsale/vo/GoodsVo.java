package com.flashsale.vo;

import com.flashsale.pojo.Goods;
import lombok.Data;
import java.util.Date;
import java.util.Objects;

@Data
public class GoodsVo extends Goods {
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;

	public static final String GOODSLIST = "GoodsList";
	public static final String GOODSDETAIL = "GoodsDetail";
	public static final String MIAOSHAGOODSCOUNT = "MiaoshaGoodsCount";
	public static final int GoodsVo_EXPIRES  = 30;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GoodsVo)) return false;
		if (!super.equals(o)) return false;
		GoodsVo goodsVo = (GoodsVo) o;
		return Objects.equals(miaoshaPrice, goodsVo.miaoshaPrice) &&
				Objects.equals(stockCount, goodsVo.stockCount) &&
				Objects.equals(startDate, goodsVo.startDate) &&
				Objects.equals(endDate, goodsVo.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), miaoshaPrice, stockCount, startDate, endDate);
	}
}
