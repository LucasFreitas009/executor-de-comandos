package br.com.lucas.majora.model.response;

public class MajoraResponse {
	private String model;
	private String created_at;
	private Message message;
	private boolean done;
	private String done_reason;
	private long total_duration;
	private long load_duration;
	private int prompt_eval_count;
	private long prompt_eval_duration;
	private int eval_count;
	private long eval_duration;

	
	//Métodos referentes a classe ResponseBody
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getDone_reason() {
		return done_reason;
	}

	public void setDone_reason(String done_reason) {
		this.done_reason = done_reason;
	}

	public long getTotal_duration() {
		return total_duration;
	}

	public void setTotal_duration(long total_duration) {
		this.total_duration = total_duration;
	}

	public long getLoad_duration() {
		return load_duration;
	}

	public void setLoad_duration(long load_duration) {
		this.load_duration = load_duration;
	}

	public int getPrompt_eval_count() {
		return prompt_eval_count;
	}

	public void setPrompt_eval_count(int prompt_eval_count) {
		this.prompt_eval_count = prompt_eval_count;
	}

	public long getPrompt_eval_duration() {
		return prompt_eval_duration;
	}

	public void setPrompt_eval_duration(long prompt_eval_duration) {
		this.prompt_eval_duration = prompt_eval_duration;
	}

	public int getEval_count() {
		return eval_count;
	}

	public void setEval_count(int eval_count) {
		this.eval_count = eval_count;
	}

	public long getEval_duration() {
		return eval_duration;
	}

	public void setEval_duration(long eval_duration) {
		this.eval_duration = eval_duration;
	}
	
	//Métodos referentes a classe Message
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}
	
	
	public void setMessageContent(String content) {
		message.setContent(content);
	}

	public static class Message {
		private String role;
		private String content;

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
