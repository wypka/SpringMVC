package controllers.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import dao.NoticeDao;
import vo.Notice;

public class NoticeDetailController implements Controller {
	private NoticeDao noticeDao;
	
	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//클릭해서 넘어오므로 빈문자열 검사는 작성하지 않는다.
		String seq= request.getParameter("seq");
		Notice notice = noticeDao.getNotice(seq);
		ModelAndView mv = new ModelAndView("noticeDetail.jsp");
		mv.addObject("notice", notice);
		return mv;
	}

}
