import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { ChevronRight } from 'lucide-react'

function AuthScreen() {
  const [email, setEmail] = useState("")
  const navigate = useNavigate();

  const handleFormSubmit = (e) => {
    e.preventDefault;
    navigate("/signup?email=" + email)
  }
  return (
    <div className='relative hero-bg'>
      <header className='max-w-6xl mx-auto flex items-center justify-between p-4'>

        <Link to={'/'}>
          <img src="/netflix-logo.png" alt="logo" className='w-52' />
        </Link>
        <Link to={"/login"} className='py-1 px-2 bg-red-600 text-white rounded hover:bg-red-700'>
          Sign In
        </Link>
      </header>
      <div className='flex flex-col items-center justify-center text-center py-40 text-white max-w-6xl mx-auto '>
        <h1 className='text-4xl md:text-6xl font-bold mb-4'>Unlimited movies, TV shows, and more</h1>
        <p className='text-lg mb-4'>Watch anywhere. Cancel anytime.</p>
        <p className='mb-4'>Ready to watch? Enter your email to create or restart your membership.</p>


        <form className='flex flex-col md:flex-row gap-4 w-1/2' onSubmit={handleFormSubmit}>
          <input
            type="email"
            placeholder='Email Address'
            className='p-2 border flex-1 border-gray-700 rounded bg-black/80'
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button className='text-xl lg:text-2xl px-2 lg:px-6 py-1 md:py-2 bg-red-600 rounded flex justify-center items-center'>
            Get Started
            <ChevronRight className='size-8 md:size-10' />
          </button>
        </form>
      </div>
      <div className='h-2 w-full bg-[#232323] aria-hidden:true'></div>

      {/* section-1 */}

      <div className='text-white  bg-black py-10'>
        <div className='flex max-w-6xl mx-auto items-center justify-center md:flex-row flex-col px-4 md:px-2'>
          <div className='flex-1 text-center md:text-left'>
            <h2 className='text-4xl md:text-5xl font-extrabold mb-4'>Enjoy on your TV</h2>
            <p className='text-lg md:text-xl'>Watch on Smart TVs, PlayStation, Xbox, Chromcast, Apple TV, Blue-ray players, and more.</p>
          </div>
          <div className='flex-1 relative'>
            <img src="/tv.png" alt="TV image" className='mt-4 z-10 relative' />
            <video
              className='absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 h-1/2'
              muted
              autoPlay={true}
              playsInline
              loop>
              <source src='/hero-vid.m4v' type='video/mp4' />
            </video>
          </div>
        </div>
      </div>

      <div className='h-2 w-full bg-[#232323] aria-hidden:true'></div>

      {/* section-2 */}

      <div className='text-white  bg-black py-10'>
        <div className='flex max-w-6xl mx-auto items-center justify-center md:flex-row flex-col-reverse px-4 md:px-2'>

          <div className='flex-1 relative'>
            <img src="/stranger-things-lg.png" alt="TV image" className='mt-4' />

            <div className='flex items-center  gap-2 absolute bottom-5 left-1/2 -translate-x-1/2  w-3/4 lg:w-1/2 bg-black border  border-slate-500 rounded-md  justify-center px-2 h-24'>
              <img src="stranger-things-sm.png" alt="stranger things small banner" className='h-full py-2' />
              <div className='flex justify-between items-center w-full'>
                <div className='flex flex-col gap-0'>
                  <span className='text-md lg:text-lg font-bold'>Stranger Things</span>
                  <span className='text-sm text-blue-500'>Downloading...</span>
                </div>
                <img src="/download-icon.gif" alt="" className='h-12'/>
              </div>
            </div>

          </div>

          <div className='flex-1 md:text-left text-center'>
            <h2 className='text-4xl md:text-5xl font-extrabold mb-4 text-balance'>Download your shows to watch online</h2>
            <p className='text-lg md:text-xl'>Save your favourites easily and always have something to watch.</p>
          </div>
        </div>
      </div>

      <div className='h-2 w-full bg-[#232323] aria-hidden:true'></div>

      {/* section-3 */}

      <div className='text-white  bg-black py-10'>
        <div className='flex max-w-6xl mx-auto items-center justify-center md:flex-row flex-col px-4 md:px-2'>
          
          <div className='flex-1 text-center md:text-left text-balance'>
            <h2 className='text-4xl md:text-5xl font-extrabold mb-4'>Watch everywhere</h2>
            <p className='text-lg md:text-xl'>Stream unlimited movies and TV shows on your phone, tablet, laptop, and TV.</p>
          </div>

          <div className='flex-1 relative overflow-hidden'>
            <img src="/device-pile.png" alt="Device image" className='mt-4 z-10 relative' />
            <video
              className='absolute top-2 left-1/2 -translate-x-1/2 h-4/6 max-w-[63%]'
              muted
              autoPlay={true}
              playsInline
              loop>
              <source src='/video-devices.m4v' type='video/mp4' />

            </video>
          </div>

        </div>
      </div>

      <div className='h-2 w-full bg-[#232323] aria-hidden:true'></div>

      {/* section-4 */}

      <div className='text-white  bg-black py-10'>
        <div className='flex max-w-6xl mx-auto items-center justify-center md:flex-row flex-col-reverse px-4 md:px-2'>

          <div className='flex-1 relative'>
            <img src="/kids.png" alt="Profiles" className='mt-4' />
          </div>

          <div className='flex-1 md:text-left text-center'>
            <h2 className='text-4xl md:text-5xl font-extrabold mb-4'>Create profiles for kids</h2>
            <p className='text-lg md:text-xl'>Send kids on adventures with their favourite characters in a space made just for them-free with your membership.</p>
          </div>
        </div>
      </div>

    </div>
  )
}

export default AuthScreen
