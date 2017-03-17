@AroundInvoke
	public Object validate(InvocationContext ctx) throws Exception {

		String[] regexs = {"[\"';!\\-<>=&{()}]+"}; // Insert OWASP rules here.
		
		RegexValidator r = new RegexValidator(regexs);
		Method m = ctx.getMethod();
		Class<?>[] pTypes = m.getParameterTypes();

		for (Class<?> c : pTypes) {
			Method[] ms = c.getMethods();
			for (Method m1 : ms) {
				if (m1.getName().matches(".*get.*")) {
					Object[] os = ctx.getParameters();
					for (Object o : os) {
						Method m2 = o.getClass().getMethod(m1.getName());
						Object o1 = m2.invoke(o, new Object[] {});
						if(r.contains(o1.toString())){
							throw new ServiceException("POST inputs contain invalid characters.");
						}
					}
				}
			}
		}

		return ctx.proceed();
	}
