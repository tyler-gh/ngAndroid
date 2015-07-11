package com.github.davityle.ngprocessor.attrcompiler.parse;

public class StepResult
{
	private State state;
	private TokenType tokenType;
	
	public StepResult(State state, TokenType tokenType)
	{
		this.state = state;
		this.tokenType = tokenType;
	}
	
	public StepResult(State state)
	{
		this.state = state;
		this.tokenType = TokenType.NONE;
	}
	
	public TokenType getTokenType()
	{
		return tokenType;
	}
	
	public State getState()
	{
		return state;
	}
}