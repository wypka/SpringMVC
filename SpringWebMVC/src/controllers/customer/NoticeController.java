package controllers.customer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import dao.NoticeDao;
import vo.Notice;

public class NoticeController implements Controller {
	private NoticeDao noticeDao;
	
	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String _page = request.getParameter("pg");
		String _field = request.getParameter("f");
		String _query = request.getParameter("q");
		
		int page = 1;
		String field = "TITLE";
		String query = "%%";
		

		if(_page != null && !_page.equals("")){
			 page = Integer.parseInt(_page);
		}
		
		if(_field !=null && !_field.equals("")){
			field = _field;
		}
		
		if(_query !=null && !_query.equals("")){
			query = _query;
		}
		/*NoticeDao dao = new NoticeDao();*/
		List<Notice> list = noticeDao.getNotices(page, field, query);
		
		ModelAndView mv = new ModelAndView("notice.jsp");
		mv.addObject("list",list);
		return mv;
	}
	
}
