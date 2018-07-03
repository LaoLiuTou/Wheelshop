package com.wheelshop.service.dstate;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.wheelshop.dao.dstate.IDstateMapper;
import com.wheelshop.model.dstate.Dstate;
public class DstateServiceImpl  implements IDstateService {

	@Autowired
	private IDstateMapper iDstateMapper;
	/**
	* 通过id选取
	* @return
	*/
	public Dstate selectDstateById(String id){
		return iDstateMapper.selectdstateById(id);
	}

	/**
	* 通过查询参数获取信息
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectDstateByParam(Map paramMap){ 
		return iDstateMapper.selectdstateByParam(paramMap);
	}
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectAllDstateByParam(Map paramMap){ 
		return iDstateMapper.selectalldstateByParam(paramMap);
	}
	@SuppressWarnings("rawtypes")
	public List<Dstate> selectStatisticdstateByParam(Map paramMap){ 
		return iDstateMapper.selectStatisticdstateByParam(paramMap);
	}

	/**
	* 通过查询参数获取总条数
	* @return
	*/ 
	@SuppressWarnings("rawtypes")
	public int selectCountDstateByParam(Map paramMap){ 
		return iDstateMapper.selectCountdstateByParam(paramMap);
	}

	/**
	* 更新 
	* @return 
	*/ 
	@Transactional
	public  int updateDstate(Dstate dstate){
		return iDstateMapper.updatedstate(dstate);
	}

	/**
	* 添加 
	* @return
	*/ 
	@Transactional
	public  int addDstate(Dstate dstate){
		return iDstateMapper.adddstate(dstate);
	}

	/**
	* 批量添加 
	* @return
	*/ 
	@Transactional
	public  int muladdDstate(List<Dstate> list){
		return iDstateMapper.muladddstate(list);
	}

	/**
	* 删除 
	* @return 
	*/ 
	@Transactional
	public  int deleteDstate(String id){
		return iDstateMapper.deletedstate(id);
	}

}

