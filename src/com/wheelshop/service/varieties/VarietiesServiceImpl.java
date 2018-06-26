package com.wheelshop.service.varieties;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.varieties.IVarietiesMapper;
import com.wheelshop.model.varieties.Varieties;
public class VarietiesServiceImpl  implements IVarietiesService {

	@Autowired
	private IVarietiesMapper iVarietiesMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Varieties selectVarietiesById(String id){
		return iVarietiesMapper.selectvarietiesById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Varieties> selectVarietiesByParam(Map paramMap){ 
		return iVarietiesMapper.selectvarietiesByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountVarietiesByParam(Map paramMap){ 
		return iVarietiesMapper.selectCountvarietiesByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateVarieties(Varieties varieties){
		return iVarietiesMapper.updatevarieties(varieties);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addVarieties(Varieties varieties){
		return iVarietiesMapper.addvarieties(varieties);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdVarieties(List<Varieties> list){
		return iVarietiesMapper.muladdvarieties(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteVarieties(String id){
		return iVarietiesMapper.deletevarieties(id);
	}

}

