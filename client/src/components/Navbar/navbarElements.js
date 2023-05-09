import { NavLink as Link } from 'react-router-dom'
import styled from 'styled-components'

export const Nav = styled.nav`
  font-size: 2vw;
  font-family: sans-serif;
  background: #658864;
  display: flex;
  justify-content: space-between;
  padding: 2vw;
  height: 0.5vw;
`

export const NavMenu = styled.div`
  display: flex;
  align-items: center;
  font-size: 1.5vw;
`

export const NavLogo = styled.div`
  color: #333333;
  padding: 0 1vw;
  display: flex;
  align-items: center;
  white-space: nowrap;
  font-weight: bold;
`

export const NavLink = styled(Link)`
  color: #B7B78A;
  display: flex;
  align-items: center;
  text-decoration: none;
  font-weight: bold;
  padding: 0 1vw;
  height: 100%;
  cursor: pointer;
  &.active {
    color: #DDDDDD;
  };
  font-size: 1.5vw;
`
