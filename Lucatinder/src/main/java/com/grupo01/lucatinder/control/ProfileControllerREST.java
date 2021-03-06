package com.grupo01.lucatinder.control;

import java.util.List;
import java.util.Optional;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.grupo01.lucatinder.exception.ProfileNotFoundException;
import com.grupo01.lucatinder.models.Profile;
import com.grupo01.lucatinder.services.ProfileService;

@RestController
@RequestMapping("/rest/profile")
public class ProfileControllerREST {

	@Autowired
	private ProfileService profileServ;
	
	@Autowired
	private BCryptPasswordEncoder codificador;	
	
	@Bean 
	public BCryptPasswordEncoder codificadorClave() {
		return new BCryptPasswordEncoder();
	}

	private int actualUserID;

	private static final Logger logger = LoggerFactory.getLogger(ProfileControllerREST.class);

	/**
	 * @author MC
	 * @param model
	 * 
	 */
	@GetMapping("/home")
	public List<Profile> getProfileSelection() throws Exception {
		logger.info("-- en HOME --");
		return profileServ.getProfileSelection(actualUserID);
	}

	@GetMapping("/like/{id}")
	Boolean likeProfile(@PathVariable int id) {
		return this.profileServ.likeProfile(actualUserID, id);
	}

	@GetMapping("/dislike/{id}")
	Boolean dislikeProfile(@PathVariable int id) {
		return this.profileServ.dislikeProfile(actualUserID, id);
	}

	/**
	 * @author MJ
	 */
	@PostMapping
	public ResponseEntity<?> addProfile(@RequestBody Profile profile) {
		Profile result = this.profileServ.addProfile(profile);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId_profile()).toUri();

		return ResponseEntity.created(location).build();
	}

	/**
	 * 
	 * @author AR
	 */
	@GetMapping("/login/{name}/{pass}")
	public Profile loginUser(@PathVariable("name") String name, @PathVariable("pass") String pass) {
		logger.info("-- Comprobando si el usuario existe --");
		Profile p = profileServ.getProfile(name).orElseThrow(ProfileNotFoundException::new);
		if (p != null && codificador.matches(pass, p.getPassword())) {
			this.actualUserID = p.getId_profile();
		}else {
			throw new ProfileNotFoundException();
		}
		return p;
	}

	/**
	 * 
	 * @author AR
	 */
	@GetMapping("/contacts")
	public List<Profile> getContacts() {
		logger.info("--listando contactos");
		return profileServ.getContactList(actualUserID);

	}
	
	/**
	 * 
	 * @author AR
	 */
	
	@PutMapping("/edit")
	public ResponseEntity<Object> editProfile(@RequestBody Profile p){
		this.profileServ.updateProfile(p);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @author AR
	 */
	
	@GetMapping("/discards")
	public List<Profile> getDiscards() {
		logger.info("--listando discards");
		return profileServ.getDiscardList(actualUserID);

	}

	@DeleteMapping("/delete")
	public void delete() {
		logger.info("-- en DELETE");
		profileServ.deleteProfile(actualUserID);
	}

	@GetMapping("/myprofile")
	public Optional<Profile> ShowProfile() {
		logger.info("--listando contactos");
		return profileServ.getProfileId(actualUserID);
	}

	/**
	 * @author MJ
	 */

	@GetMapping("/matches")
	public List<Profile> getMatches() {
		logger.info("--listando matches");
		return profileServ.getMatchesList(actualUserID);
	}

}
