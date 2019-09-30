package uas.project.maven.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uas.project.maven.model.TabunganModel;
import uas.project.maven.repository.TabunganRepository;

@Service
public class TabunganDao {
	@Autowired
	TabunganRepository tabunganRepository;
	
	//get all
	public List<TabunganModel> readAll(){
		return tabunganRepository.findAll();
	}
	
	//save
	public TabunganModel create(TabunganModel tabunganModel) {
		TabunganModel tm=tabunganModel;
		TabunganModel tmd=tabunganRepository.getSaldo(tabunganModel.getNik());
		if(tmd==null) {
			tm.setSaldo(0-tm.getDebet()+tm.getKredit());
			return tabunganRepository.save(tm);
		}else {
			tm.setSaldo(tmd.getSaldo()-tm.getDebet()+tm.getKredit());
			return tabunganRepository.save(tm);
		}
	}
	
	//find by id
	public TabunganModel readById(Long id) {
		return tabunganRepository.findOne(id);	
	}
	
	//delete
	public void delete(Long id) {
		tabunganRepository.delete(id);
		
	}
	
	//read by nik
	public List<TabunganModel> readByNik(String nik){
		return tabunganRepository.readByNik(nik);
	}
	
	public TabunganModel getFindSaldo(String nik) {
		return tabunganRepository.getSaldo(nik);
	}
	
	//update saldo
	public TabunganModel updateSaldo(TabunganModel tabunganModel) {
		TabunganModel tbng_mod=tabunganModel;
		TabunganModel tabung=tabunganRepository.findOne(tbng_mod.getId());
		tabung.setSaldo(tabung.getSaldo()+tbng_mod.getKredit()-tbng_mod.getDebet());
		tabung.setDebet(tbng_mod.getDebet());
		tabung.setKredit(tbng_mod.getKredit());
		int hasil=tabung.getSaldo();
		List <TabunganModel> dataList=tabunganRepository.readByNik(tbng_mod.getNik());
		for(TabunganModel data : dataList) {
			if(data.getId() > tbng_mod.getId()) {
				TabunganModel secresult=tabunganRepository.findOne(data.getId());
				secresult.setSaldo(hasil+secresult.getKredit()-secresult.getDebet());
				tabunganRepository.save(secresult);
				hasil=secresult.getSaldo();
				
			}
		}
		return tabunganRepository.save(tabung);
		
	}
	
	//delete saldo
	public void deleteSaldo(Long id) {
		TabunganModel tbm=tabunganRepository.findOne(id);
		List <TabunganModel> dataList= tabunganRepository.readByNik(tbm.getNik());
		for(TabunganModel data : dataList) {
			if(data.getId() > id) {
				TabunganModel result=tabunganRepository.findOne(data.getId());
				result.setSaldo(result.getSaldo()+tbm.getKredit()-tbm.getDebet());
				tabunganRepository.save(tbm);
			}
		}
		tabunganRepository.delete(id);
	} 
	
	
}
