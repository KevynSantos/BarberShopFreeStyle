package br.com.BarberShopFreeStyle.controllers;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import br.com.BarberShopFreeStyle.enums.Access;
import br.com.BarberShopFreeStyle.enums.TypeProfile;
import br.com.BarberShopFreeStyle.models.Usuario;
import br.com.BarberShopFreeStyle.services.UserService;

@Controller
public class AbstractController {
	
	public Usuario getUserFromSession(HttpSession session)
	{
		final Object obj = session.getAttribute( "userId" );

		Long id = null;
		if ( obj instanceof Long )
		{
			id = ( Long ) obj;
		}
		else
		{
			throw new IllegalStateException( "No user logged" );
		}

		
		final Usuario user = userService.getById( id );
		

		return user;
		
	}
	
	public ModelAndView checkPermissionProfile(Usuario user, Access funcionality, ModelAndView view) 
	{
		if((funcionality.equals(Access.PODE_AGENDAR) && !user.getFuncionario().isMarcaHora() && user.getTipoPerfil().getTipo() != TypeProfile.ADMINISTRADOR) 
				|| (funcionality.equals(Access.NAO_PODE_AGENDAR) && user.getFuncionario().isMarcaHora() && user.getTipoPerfil().getTipo() != TypeProfile.ADMINISTRADOR))
		{
			view = new ModelAndView("redirect:/home");
			
			view.addObject( "message" , "notAcess");
			
			return view;
		}
		
		return view;
	}
	
	public ModelAndView checkPermissionManagement(Usuario user, ModelAndView view)
	{
		if(!user.getTipoPerfil().getTipo().equals( TypeProfile.ADMINISTRADOR ))
		{
			view = new ModelAndView("redirect:/home");
			
			view.addObject( "message" , "notAcess");
			
			return view;
		}
		
		return view;
	}
	
	
	
	@Autowired
	private UserService userService;

}
