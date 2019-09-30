package uas.project.maven.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import uas.project.maven.dao.TabunganDao;
import uas.project.maven.model.TabunganModel;

@RestController
@RequestMapping("/bank")
public class TabunganController {
	@Autowired
	TabunganDao tabunganDao;
	
	//for save
	@PostMapping("/tabungan")
	public TabunganModel saveTabungan(@Valid @RequestBody TabunganModel tabunganModel) {
		return tabunganDao.create(tabunganModel);
	}
	
	//for get all
	@GetMapping("/tabungan")
	public List<TabunganModel> getAllTabungan(){
		return tabunganDao.readAll();
	}
	

	@GetMapping("/tabungan/{id}")
	public ResponseEntity<TabunganModel> readById(@PathVariable(value="id") Long id) {
		TabunganModel tm = tabunganDao.readById(id);
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok().body(tm);
		}
	}
	
	@PutMapping("/tabungan/{id}")
	public ResponseEntity<TabunganModel> update(@PathVariable(value="id") Long id, @Valid @RequestBody TabunganModel tabungan){
		TabunganModel tm = tabunganDao.readById(id);
		
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			tm.setNik(tabungan.getNik());
			tm.setNama(tabungan.getNama());
			tm.setSaldo(tm.getSaldo()-tm.getKredit()+tm.getDebet());
			tm.setKredit(tabungan.getKredit());
			tm.setDebet(tabungan.getDebet());
			
			TabunganModel tmResult = tabunganDao.create(tm);
			return ResponseEntity.ok().body(tmResult);
		}
	
	}
	
	@DeleteMapping("/tabungan/{id}")
	public ResponseEntity<TabunganModel> delete(@PathVariable(value="id") Long id){
		TabunganModel tm = tabunganDao.readById(id);
		
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			tabunganDao.delete(id);
			return ResponseEntity.ok().build();
		}
	}
	
	
	// read  by nik
	@GetMapping("/tabungan1/{nik}")
	public List<TabunganModel> getByNik(@PathVariable(value="nik")String nik){
		return tabunganDao.readByNik(nik);
	}
	
	// get saldo
	@GetMapping("/saldo/{nik}")
	public ResponseEntity<TabunganModel> getTabung (@PathVariable(value="nik")String nik){
		TabunganModel tm=tabunganDao.getFindSaldo(nik);
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok().body(tm);
		}
	}
	
	@PutMapping("/ubahSaldo/{id}")
	public ResponseEntity<TabunganModel> updateSaldo(@PathVariable(value="id")Long id,@Valid @RequestBody TabunganModel tabunganModel){
		TabunganModel tm=tabunganDao.readById(id);
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			tm.setSaldo(tm.getSaldo()+tm.getDebet()-tm.getKredit());
			tm.setKredit(tabunganModel.getKredit());
			tm.setDebet(tabunganModel.getDebet());
			TabunganModel result=tabunganDao.updateSaldo(tm);
			return ResponseEntity.ok().body(result);
		}
	}
	
	@DeleteMapping("/hapusSaldo/{id}")
	public ResponseEntity<TabunganModel> deleteSaldo(@PathVariable(value="id")Long id){
		TabunganModel tm=tabunganDao.readById(id);
		if(tm==null) {
			return ResponseEntity.notFound().build();
		}else {
			tabunganDao.deleteSaldo(id);
			return ResponseEntity.ok().build();
		}
	}
}
